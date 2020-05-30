package com.haisheng.framework.testng.commonCase;

import java.lang.reflect.Method;

/**
 * @author : yu
 * @date :  2020/05/30
 */
public interface TestCaseStd {

    void initial();
    void clean();

    void createFreshCase(Method method);
}
