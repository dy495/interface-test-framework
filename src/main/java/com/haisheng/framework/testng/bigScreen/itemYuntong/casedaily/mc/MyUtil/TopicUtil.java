package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.MyUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class TopicUtil extends SceneUtil {


    public TopicUtil(VisitorProxy visitor) {
        super(visitor);
    }

    public JSONObject setType(String typeName,JSONArray items){
        HashMap<String, Integer> typeMap = new HashMap<>();
        typeMap.put("欢迎接待",100);
        typeMap.put("个性需求",200);
        typeMap.put("新车推荐",300);
        typeMap.put("试乘试驾",400);
        typeMap.put("车辆提案",500);
        JSONObject type = new JSONObject();
        type.put("type",typeMap.get(typeName));
        type.put("type_name",typeName);
        type.put("items",items);
        return type;
    }
    public JSONObject setOneTopic(String title, @NotNull String... content){
        JSONObject item = new JSONObject();
        int num = content.length;
        item.put("answer_number",num);
        item.put("title",title);
        item.put("answers",putAnswers(content));
        return item;
    }

    public JSONArray putAnswers(String... content){
        JSONArray item = new JSONArray();
        if(content.length==2){
            item.add(setAnswer(5,content[0]));
            item.add(setAnswer(1,content[1]));
        }else if(content.length==3){
            item.add(setAnswer(5,content[0]));
            item.add(setAnswer(3,content[1]));
            item.add(setAnswer(1,content[2]));
        } else if(content.length==5){
            item.add(setAnswer(5,content[0]));
            item.add(setAnswer(4,content[1]));
            item.add(setAnswer(3,content[2]));
            item.add(setAnswer(2,content[3]));
            item.add(setAnswer(1,content[4]));
        }

        return item;
    }

    public JSONObject setAnswer(Integer score, String content){
        JSONObject answer = new JSONObject();
        answer.put("score",score);
        answer.put("content", content);
        return answer;
    }

    public JSONArray checkContents(String title,String... answer){
        JSONArray links = new JSONArray();
        JSONArray items = new JSONArray();
        items.add(setOneTopic(title,answer));
        links.add(setType("欢迎接待",items));
        links.add(setType("个性需求",items));
        links.add(setType("新车推荐",items));
        links.add(setType("试乘试驾",items));
        links.add(setType("车辆提案",items));
        return links;
    }





}