package com.haisheng.framework.testng.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class AlgorithomMergePush {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    private void test() {
        String[] atMobiles = {"1", "2"};
        String textMsg = "{" +
                "\"msgtype\": \"markdown\"," +
                "\"markdown\": {\"title\":\"QA推送\"," +
                "\"text\": \"" + "mmmmmmmmmmmmmmmmmssssssssssssssssssssgggggggggggggggg" + "\"" +
                "}" +
                    ",\"at\": {" +
                                "\"atMobiles\": [" +
                                                    strArrayToString(atMobiles) +
                                                "], " +
                                "\"isAtAll\": false" +
                    "}}";

        logger.info(textMsg);
    }

    private String strArrayToString(String[] strArray) {
        String result = "";
        int size = strArray.length;

        if (1 == size) {
            result = "\"" + strArray[0] + "\"";
        } else {
            //>1
            for (int i=0; i<size; i++) {
                if (i < size-1) {
                    result += "\"" + strArray[i] + "\",";
                } else {
                    result += "\"" + strArray[i] + "\"";
                }
            }
        }

        return result;
    }
}
