package com.haisheng.framework.testng.alarm;

import com.haisheng.framework.util.DingChatbot;
import org.testng.annotations.Test;

public class DingdingAlarmCi {

    @Test
    public void dingdingAlarm() {
        String detail = "请及时触发CI，运行自动化回归";
        //screenshot do not support local pic, must use pic in web
        String bugPic = "http://i01.lw.aliimg.com/media/lALPBbCc1ZhJGIvNAkzNBLA_1200_588.png";
        String linkUrl = "http://192.168.50.2:8080/view/云端测试/job/pv-cloud-test";
        String msg = DingChatbot.getAlarmMarkdown("business-flow代码变动", detail, bugPic, linkUrl);
        DingChatbot.sendMarkdown(msg);
    }

}
