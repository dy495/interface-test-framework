package com.haisheng.framework.util;


/**
 * @author yu
 * @description QADbUtil的代理，用于兼容以前的代码对QADbUtil的直接引用
 */

public class QADbProxy {


    /**
     * 单利，确保多个类共用一份db session
     *
     * */

    private static volatile QADbProxy instance = null;
    private static QADbUtil qaDbUtil = null;

    private QADbProxy() {}


    public static QADbProxy getInstance() {

        if (null == instance) {
            synchronized (QADbProxy.class) {
                if (null == instance) {
                    instance = new QADbProxy();
                    qaDbUtil = new QADbUtil();
                }
            }
        }

        System.out.println("db proxy instance: " + instance);
        return instance;
    }

    public QADbUtil getQaUtil() {
        return qaDbUtil;
    }

}
