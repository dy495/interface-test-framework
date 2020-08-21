package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * 钉钉群机器人
 *
 * @author wangmin
 * @date 2020/7/24 15:17
 */
public enum EnumDingTalkWebHook {
    /**
     * QA测试群
     */
    QA_TEST_GRP("https://oapi.dingtalk.com/robot/send?access_token=0732a60532e16e85c37dcbbd350d461d51e5b877b6e4cd7aba498acffdf1c175"),
    /**
     * 日常回归-开放平台&独立项目
     */
    OPEN_MANAGEMENT_PLATFORM_GRP("https://oapi.dingtalk.com/robot/send?access_token=8a9eb2c50f9b3a62bca0d9963f6a7938d7f06df6ba62889673432448ea6abb4a"),
    ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP("https://oapi.dingtalk.com/robot/send?access_token=3d327456dd1da2b32ce7f30c67e2242e02305a1321cfa1744afee3ed9634dbbe"),
    COMMODITY_SHELF_GRP("https://oapi.dingtalk.com/robot/send?access_token=225c6810c977c616c9c4c112e9205b7f3f7204392c4388a05ff708cbe9e4a6ee"),
    PV_UV_ACCURACY_GRP("https://oapi.dingtalk.com/robot/send?access_token=0837493692d7a7e41f6da3fda6ed8e42f8015210b1fad450a415afbcbc7b5907"),
    DAILY_PV_UV_ACCURACY_GRP("https://oapi.dingtalk.com/robot/send?access_token=6f964c9d8415010e246613f77ec13a2983f4e7958be38f68d3a1f93bfabc5028"),
    CLOUD_ALARM_GRP("https://oapi.dingtalk.com/robot/send?access_token=be30edcb935927a9b07aecac5e1483413b8e5ef340ab1534dc82077aa3ea60ae"),
    APP_CLOUD_ALARM_GRP("https://oapi.dingtalk.com/robot/send?access_token=9607ea62b6f638bc86fe8676c2f896c52f5d0f500562b2e1673cfb887e649433"),
    APP_BAIGUOYUAN_ALARM_GRP("https://oapi.dingtalk.com/robot/send?access_token=a6af5a19fd5069d6fcc34effc80f0b777d0850b3302f0d52e0a950d5cb39d5ae"),
    DAILY_EDGE("https://oapi.dingtalk.com/robot/send?access_token=4e774f32136b1404328315a84688535a55c8e7440850339c8fccde156a23bd3b");

    EnumDingTalkWebHook(String webHook) {
        this.webHook = webHook;
    }

    @Getter
    private final String webHook;
}
