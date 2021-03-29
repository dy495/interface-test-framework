package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.activity;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 活动生成入口
 *
 * @author wangmin
 * @date 2021/1/20 14:54
 */
public class ActivityGenerator extends AbstractActivity {

    public ActivityGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(VisitorProxy visitor, IScene scene) {

    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder {

        @Override
        public IActivity buildActivity() {
            return new ActivityGenerator(this);
        }

    }
}
