package com.haisheng.framework.testng.bigScreen.jiaochen.wm.activity;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.AbstractGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * 活动生成器抽象类
 *
 * @author wangmin
 * @date 2021/1/22 13:45
 */
public abstract class BaseActivity extends AbstractGenerator implements IActivity {
    protected BaseActivity(@NotNull AbstractBuilder<?> abstractBuilder) {
        super(abstractBuilder);
    }

    @Override
    public Long getActivityId() {
        return null;
    }

    @Override
    public abstract void execute(Visitor visitor);
}
