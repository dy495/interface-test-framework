package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.MyUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.inject.internal.util.$SourceProvider;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;

public class TopicUtil extends SceneUtil {


    public TopicUtil(VisitorProxy visitor) {
        super(visitor);
    }
    /*
    {
    "links":[
        Object{...},
        Object{...},
        Object{...},
        Object{...},
        Object{...}
        ]
    }
    */


    /*
        {
            "type":100,
            "type_name":"欢迎接待",
            "items":[
                Object{...}
            ]
        }
    */
    private JSONObject setType(String typeName, JSONArray items) {
        HashMap<String, Integer> typeMap = new HashMap<>();
        typeMap.put("欢迎接待", 100);
        typeMap.put("个性需求", 200);
        typeMap.put("新车推荐", 300);
        typeMap.put("试乘试驾", 400);
        typeMap.put("车辆提案", 500);
        JSONObject type = new JSONObject();
        type.put("type", typeMap.get(typeName));
        type.put("type_name", typeName);
        type.put("items", items);
        return type;
    }

    /*
        {
        "title":"11111111",
        "answer_number":2,
        "answers":[
            Object{...},
            Object{...}
            ]
        }
    * */


    private JSONObject setOneTopic(String title, @NotNull String... content) {
        JSONObject item = new JSONObject();
        int num = content.length;
        item.put("answer_number", num);
        item.put("title", title);
        item.put("answers", putAnswers(content));
        return item;
    }
    /*
     "answers":[
            Object{...},
            Object{...}
            ]
    */

    private JSONArray putAnswers(String... content) {
        JSONArray item = new JSONArray();
        if (content.length == 2) {
            item.add(setAnswer(5, content[0]));
            item.add(setAnswer(1, content[1]));
        } else if (content.length == 3) {
            item.add(setAnswer(5, content[0]));
            item.add(setAnswer(3, content[1]));
            item.add(setAnswer(1, content[2]));
        } else if (content.length == 5) {
            item.add(setAnswer(5, content[0]));
            item.add(setAnswer(4, content[1]));
            item.add(setAnswer(3, content[2]));
            item.add(setAnswer(2, content[3]));
            item.add(setAnswer(1, content[4]));
        }

        return item;
    }
    /*
       {
        "score":5,
        "content":"555"
       }
     */

    private JSONObject setAnswer(Integer score, String content) {
        JSONObject answer = new JSONObject();
        answer.put("score", score);
        answer.put("content", content);
        return answer;
    }

    public JSONArray checkContents(String title, String... answer) {
        JSONArray links = new JSONArray();
        JSONArray items = new JSONArray();
        items.add(setOneTopic(title, answer));
        links.add(setType("欢迎接待", items));
        links.add(setType("个性需求", items));
        links.add(setType("新车推荐", items));
        links.add(setType("试乘试驾", items));
        links.add(setType("车辆提案", items));
        return links;
    }

    public JSONArray checkTopicNum(List<Integer> topicNum, String... answer) {
        JSONArray links = new JSONArray();
        JSONArray item1 = new JSONArray();
        JSONArray item2 = new JSONArray();
        JSONArray item3 = new JSONArray();
        JSONArray item4 = new JSONArray();
        JSONArray item5 = new JSONArray();
        if (topicNum.size() == 5) {
            if(topicNum.get(0)>0){
            for (int i = 1; i <= topicNum.get(0); i++) {
                item1.add(setOneTopic("欢迎接待题目"+i, answer));
            }}
            if(topicNum.get(1)>0){
            for (int i = 1; i <= topicNum.get(1); i++) {
                item2.add(setOneTopic("个性需求题目"+i, answer));
            }}
            if(topicNum.get(2)>0){
            for (int i = 1; i <= topicNum.get(2); i++) {
                item3.add(setOneTopic("新车推荐题目"+i, answer));
            }}
            if(topicNum.get(3)>0){
            for (int i = 1; i <= topicNum.get(3); i++) {
                item4.add(setOneTopic("试乘试驾题目"+i, answer));
            }}
            if(topicNum.get(4)>0){
            for (int i = 1; i <= topicNum.get(4); i++) {
                item5.add(setOneTopic("车辆提案题目"+i, answer));
            }}
            links.add(setType("欢迎接待", item1));
            links.add(setType("个性需求", item2));
            links.add(setType("新车推荐", item3));
            links.add(setType("试乘试驾", item4));
            links.add(setType("车辆提案", item5));
        } else {
            throw new NullPointerException("参数格式不正确");
        }

        return links;
    }
}