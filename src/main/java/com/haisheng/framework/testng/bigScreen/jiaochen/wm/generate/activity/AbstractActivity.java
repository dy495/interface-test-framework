package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.activity;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ManagerPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.BaseGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManagerPageScene;

import java.util.List;

/**
 * 活动生成器抽象类
 *
 * @author wangmin
 * @date 2021/1/22 13:45
 */
public abstract class AbstractActivity extends BaseGenerator implements IActivity {
    protected ActivityStatusEnum activityStatus;
    protected final IScene activityScene;

    protected AbstractActivity(BaseBuilder baseBuilder) {
        super(baseBuilder);
        this.activityStatus = baseBuilder.activityStatus;
        this.activityScene = baseBuilder.activityScene;
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
        activityStatus.getActivityBuilder().buildActivity().execute(visitor, activityScene);
        return getActivityId();
    }

    @Override
    public abstract void execute(VisitorProxy visitor, IScene scene);

    public static abstract class BaseBuilder extends AbstractBuilder<BaseBuilder> {
        private ActivityStatusEnum activityStatus;
        private IScene activityScene;

        /**
         * @param activityStatus 活动状态
         * @return BaseBuilder.activityStatus
         */
        public BaseBuilder activityStatusEnum(ActivityStatusEnum activityStatus) {
            this.activityStatus = activityStatus;
            return this;
        }

        /**
         * 创建活动的场景
         *
         * @param activityScene 活动场景
         * @return BaseBuilder.activityScene
         */
        public BaseBuilder createScene(IScene activityScene) {
            this.activityScene = activityScene;
            return this;
        }

        public abstract IActivity buildActivity();

        @Override
        protected IActivity buildProduct() {
            return buildActivity();
        }
    }
}
