package com.haisheng.framework.testng.bigScreen.jiaochen.wm.activity;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.activity.ManagerPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.AbstractGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManagerPageScene;

import java.util.List;

/**
 * 活动生成器抽象类
 *
 * @author wangmin
 * @date 2021/1/22 13:45
 */
public abstract class BaseActivity extends AbstractGenerator implements IActivity {
    protected ActivityStatusEnum activityStatus;

    protected BaseActivity(BaseBuilder baseBuilder) {
        super(baseBuilder);
        this.activityStatus = baseBuilder.activityStatus;
    }

    @Override
    public Long getActivityId() {
        IScene scene = ManagerPageScene.builder().build();
        List<ManagerPage> managerPages = resultCollectToBean(scene, ManagerPage.class);
        ManagerPage managerPage = managerPages.stream().filter(e -> e.getStatusName().equals(activityStatus.getStatusName())).findFirst().orElse(null);
        if (managerPage != null) {
            logger("activityId is: " + managerPage.getId());
            return managerPage.getId();
        }
        logger(activityStatus.name() + " DIDN'T FIND ");
        activityStatus.getActivityBuilder().buildActivity().execute(visitor);
        return getActivityId();
    }

    @Override
    public abstract void execute(Visitor visitor);

    public static abstract class BaseBuilder extends AbstractBuilder<BaseBuilder> {
        private ActivityStatusEnum activityStatus;

        /**
         * @param activityStatus 活动状态
         * @return BaseBuilder.activityStatus
         */
        public BaseBuilder activityStatusEnum(ActivityStatusEnum activityStatus) {
            this.activityStatus = activityStatus;
            return this;
        }

        public abstract IActivity buildActivity();

        @Override
        protected IActivity buildProduct() {
            return buildActivity();
        }
    }
}
