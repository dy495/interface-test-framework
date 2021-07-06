package com.haisheng.framework.testng.bigScreen.itemBasic.base.generator;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;

/**
 * 产品生成器接口
 *
 * @author wangmin
 * @date 2021/1/20 14:36
 */
public interface IGenerator {

    /**
     * 获取最近一次的错误信息
     */
    String getErrorMsg();

    /**
     * 访问者执行
     *
     * @param visitor 访问者
     */
    void execute(VisitorProxy visitor, IScene scene);
}
