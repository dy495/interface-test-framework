package com.haisheng.framework.testng.bigScreen.jiaochen.wm.activity;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author wangmin
 * @date 2021/1/20 14:54
 * @desc 活动生成
 */
public class ActivityGenerator extends BaseActivity {

    public ActivityGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public void execute(Visitor visitor, IScene scene) {

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
