package com.haisheng.framework.util;

import com.aliyun.openservices.ons.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class MQYun {

    static Logger logger = LoggerFactory.getLogger("MQYun");
    static Consumer consumer = null;

    public static void subscribeTopic() {
        Properties properties = new Properties();
        // 您在控制台创建的 Group ID
        properties.put(PropertyKeyConst.GROUP_ID, "GID_WINSENSE_RETAIL_CUSTOMER_NOTIFY_a4d4d18741a8");
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, "LTAIlYpjA39n18Yr");
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, "fUPPfBIWeTKJp8oeVincGRjV5mt3Cg");
        // 设置 TCP 接入域名，进入控制台的实例管理页面的“获取接入点信息”区域查看
        properties.put(PropertyKeyConst.ONSAddr,
                "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
        // 集群订阅方式 (默认)
         properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);

        consumer = ONSFactory.createConsumer(properties);
//        consumer.subscribe("WINSENSE_RETAIL_CUSTOMER_NOTIFY_a4d4d18741a8", "TagA||TagB", new MessageListener() { //订阅多个 Tag
//            public Action consume(Message message, ConsumeContext context) {
//                System.out.println("Receive: " + message);
//                return Action.CommitMessage;
//            }
//        });
        //订阅另外一个 Topic
        consumer.subscribe("WINSENSE_RETAIL_CUSTOMER_NOTIFY_a4d4d18741a8", "*", new MessageListener() { //订阅全部 Tag
            public Action consume(Message message, ConsumeContext context) {
                logger.info("");
                logger.info(">>>>>>>MQ Receive: " + message);
                logger.info("");
                return Action.CommitMessage;
            }
        });
        consumer.start();
        logger.info("MQ Consumer Started");
    }

    public static void shutdown() {
        consumer.shutdown();
    }

}
