package com.haisheng.framework.testng.bigScreen.crm.wm.table;

import com.haisheng.framework.testng.bigScreen.crm.wm.property.BasicProperty;

public abstract class BaseTable extends BasicProperty implements ITable {
    /**
     * 获取方法名称
     *
     * @return 方法名称
     */
    public String getMethodName() {
        StackTraceElement[] temp = Thread.currentThread().getStackTrace();
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].getMethodName().equals("invoke0")) {
                return temp[i - 1].getMethodName();
            }
        }
        return null;
    }

    /**
     * 收集错误信息
     *
     * @param e 错误信息
     */
    public void exceptionCollect(Exception e) {
        e.printStackTrace();
        errorMsg.append(e.toString());
        if (errorMsg.length() > 0) {
            sendDing();
        }
    }

    public abstract void sendDing();
}
